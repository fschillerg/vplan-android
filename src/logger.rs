use super::android_logger::{self, Filter};
use super::log::Level;
use super::log_panics;

pub fn init() {
    log_panics::init();
    android_logger::init_once(Filter::default().with_min_level(Level::Trace), Some("rust"));
}
