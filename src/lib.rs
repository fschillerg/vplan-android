extern crate android_logger;
extern crate chrono;
extern crate jni;
extern crate libvplan;
#[macro_use]
extern crate log;
extern crate log_panics;
extern crate tokio;

mod logger;

use chrono::Weekday;
use jni::objects::{JObject, JString, JValue};
use jni::JNIEnv;
use std::alloc::System;

#[global_allocator]
static GLOBAL: System = System;

fn convert_weekday<'a>(env: &JNIEnv<'a>, weekday: JObject) -> Weekday {
    let weekday = match env.call_method(weekday, "name", "()Ljava/lang/String;", &[]) {
        Ok(name) => match name {
            JValue::Object(name) => env
                .get_string(JString::from(name))
                .expect("Couldn't convert Java string!"),
            _ => panic!("enum name is not of type `Object`")
        },
        Err(error) => panic!(error)
    };

    let weekday = match weekday.to_str() {
        Ok(string) => string,
        Err(error) => panic!(error)
    };

    match weekday {
        "MONDAY" => Weekday::Mon,
        "TUESDAY" => Weekday::Tue,
        "WEDNESDAY" => Weekday::Wed,
        "THURSDAY" => Weekday::Thu,
        "FRIDAY" => Weekday::Fri,
        "SATURDAY" => Weekday::Sat,
        "SUNDAY" => Weekday::Sun,
        _ => panic!("invalid DayOfWeek")
    }
}

fn string_to_jvalue<'a>(env: &'a JNIEnv<'a>, string: String) -> JValue {
    let string = env
        .new_string(string)
        .expect("Couldn't create Java `String`!");

    JValue::Object(JObject::from(string))
}

fn get_handle<'a>(env: &'a JNIEnv<'a>, object: JObject) -> i64 {
    match env.get_field(object, "handle", "J") {
        Ok(handle) => match handle {
            JValue::Long(handle) => handle,
            _ => panic!("handle was not of type `Long`")
        },
        Err(error) => panic!(error)
    }
}

fn set_handle<'a>(env: &'a JNIEnv<'a>, object: JObject, value: i64) {
    match env.set_field(object, "handle", "J", JValue::Long(value)) {
        Ok(_) => {},
        Err(error) => panic!(error)
    }
}

#[allow(non_snake_case)]
pub mod android {
    use super::jni::sys::{jlong, jlongArray, jobject, jobjectArray};
    use super::libvplan::{Client, Vplan, WeekType};
    use super::tokio::runtime::Runtime;
    use super::*;
    use jni::objects::JClass;
    use std::boxed::Box;

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_VplanClient_initNative(
        env: JNIEnv,
        this: JClass,
        username: JString,
        password: JString
    ) {
        logger::init();

        let username = String::from(
            env.get_string(username)
                .expect("Couldn't convert Java string!")
        );
        let password = String::from(
            env.get_string(password)
                .expect("Couldn't convert Java string!")
        );
        let client = Client::new(&username, &password);
        let handle = Box::into_raw(Box::new(client));

        set_handle(&env, JObject::from(this), handle as i64);
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_VplanClient_getNative(
        env: JNIEnv,
        this: JClass,
        weekday: JObject
    ) -> jlong {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let client = *Box::from_raw(handle as *mut Client);

        let weekday = convert_weekday(&env, weekday);

        let mut rt = match Runtime::new() {
            Ok(rt) => rt,
            Err(error) => panic!(error)
        };

        let result = match rt.block_on(client.get(weekday)) {
            Ok(vplan) => Box::into_raw(Box::new(vplan)) as i64,
            Err(error) => {
                error!("{}", error);
                0
            }
        };

        let handle = Box::into_raw(Box::new(client));
        set_handle(&env, JObject::from(this), handle as i64);

        result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_VplanClient_destroyNative(
        env: JNIEnv,
        this: JClass
    ) {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));

        let pointer = handle as *mut Client;
        if pointer.is_null() {
            warn!("object already destroyed");
        } else {
            let _ = Box::from_raw(pointer);
        }
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_getDateNative(
        env: JNIEnv,
        this: JClass
    ) -> jlong {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let vplan = *Box::from_raw(handle as *mut Vplan);

        let result = vplan.date.date.timestamp() * 1000;

        let handle = Box::into_raw(Box::new(vplan));
        set_handle(&env, JObject::from(this), handle as i64);

        result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_getWeekTypeNative(
        env: JNIEnv,
        this: JClass
    ) -> jobject {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let vplan = *Box::from_raw(handle as *mut Vplan);

        let class = env
            .find_class("de/fschillerg/vplan/vplan/WeekType")
            .expect("Couldn't find `WeekType` class!");

        let result = match vplan.date.week_type {
            WeekType::A => env.get_static_field(class, "A", "Lde/fschillerg/vplan/vplan/WeekType;"),
            WeekType::B => env.get_static_field(class, "B", "Lde/fschillerg/vplan/vplan/WeekType;")
        }
        .expect("Could not create `WeekType` variant!");

        let result = match result {
            JValue::Object(object) => object,
            _ => panic!("")
        };

        let handle = Box::into_raw(Box::new(vplan));
        set_handle(&env, JObject::from(this), handle as i64);

        *result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_getChangedNative(
        env: JNIEnv,
        this: JClass
    ) -> jlong {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let vplan = *Box::from_raw(handle as *mut Vplan);

        let result = vplan.changed.timestamp() * 1000;

        let handle = Box::into_raw(Box::new(vplan));
        set_handle(&env, JObject::from(this), handle as i64);

        result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_getDaysOffNative(
        env: JNIEnv,
        this: JClass
    ) -> jlongArray {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let vplan = *Box::from_raw(handle as *mut Vplan);

        let days: Vec<i64> = vplan.days_off.iter().map(|day| day.timestamp()).collect();

        let result = match env.new_long_array(days.len() as i32) {
            Ok(array) => array,
            Err(error) => panic!(error)
        };

        match env.set_long_array_region(result, 0, &days) {
            Ok(_) => {},
            Err(error) => panic!(error)
        }

        let handle = Box::into_raw(Box::new(vplan));
        set_handle(&env, JObject::from(this), handle as i64);

        result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_getChangesNative(
        env: JNIEnv,
        this: JClass
    ) -> jobjectArray {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let vplan = *Box::from_raw(handle as *mut Vplan);

        let class = env
            .find_class("de/fschillerg/vplan/vplan/Change")
            .expect("Couldn't find `Change` class!");

        let mut args = Vec::with_capacity(6);

        for _ in 0..6 {
            args.push(string_to_jvalue(&env, "".to_owned()));
        }

        let dummy = env.new_object(class, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", &args).expect("Couldn't create `Change` instance!");

        let result = match env.new_object_array(vplan.changes.len() as i32, class, dummy) {
            Ok(array) => array,
            Err(error) => panic!(error)
        };

        for (i, change) in vplan.changes.iter().enumerate() {
            let mut args = Vec::with_capacity(6);

            args.push(string_to_jvalue(&env, change.class.clone()));
            args.push(string_to_jvalue(&env, change.lesson.clone()));
            args.push(string_to_jvalue(&env, change.subject.clone()));
            args.push(string_to_jvalue(&env, change.teacher.clone()));
            args.push(string_to_jvalue(&env, change.room.clone()));
            args.push(string_to_jvalue(&env, change.info.clone()));

            let change = env.new_object(class, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", &args).expect("Couldn't create `Change` instance!");

            match env.set_object_array_element(result, i as i32, change) {
                Ok(_) => {},
                Err(error) => panic!(error)
            };
        }

        let handle = Box::into_raw(Box::new(vplan));
        set_handle(&env, JObject::from(this), handle as i64);

        result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_getInfoNative(
        env: JNIEnv,
        this: JClass
    ) -> jobjectArray {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));
        let vplan = *Box::from_raw(handle as *mut Vplan);

        let class = env
            .find_class("java/lang/String")
            .expect("Couldn't find `String` class!");

        let result = match env.new_object_array(
            vplan.info.len() as i32,
            class,
            JObject::from(env.new_string("").expect("Couldn't create Java `String`!"))
        ) {
            Ok(array) => array,
            Err(error) => panic!(error)
        };

        for (i, info) in vplan.info.iter().enumerate() {
            let info = env
                .new_string(info)
                .expect("Couldn't create Java `String`!");

            match env.set_object_array_element(result, i as i32, JObject::from(info)) {
                Ok(_) => {},
                Err(error) => panic!(error)
            };
        }

        let handle = Box::into_raw(Box::new(vplan));
        set_handle(&env, JObject::from(this), handle as i64);

        result
    }

    #[no_mangle]
    pub unsafe extern "C" fn Java_de_fschillerg_vplan_vplan_NativeVplan_destroyNative(
        env: JNIEnv,
        this: JClass
    ) {
        logger::init();

        let handle = get_handle(&env, JObject::from(this));

        let pointer = handle as *mut Vplan;
        if pointer.is_null() {
            warn!("object already destroyed");
        } else {
            let _ = Box::from_raw(pointer);
        }
    }
}
