extern crate pretty_env_logger;

mod objects;
mod fileio;
mod iterate;

use std::env;
use std::fmt::Debug;
use clap::Parser;
use crate::objects::NumberPair;

#[derive(Parser, Debug)]
#[command(author, version, about, long_about = None)]
struct ProgArgs {
    #[arg(short, long)]
    input: String,

    #[arg(long, default_value_t = 11)]
    max_width: i32,

    #[arg(long, default_value_t = 7)]
    max_height: i32,

    #[arg(long, default_value_t = 100)]
    iterations: i32,
}

/// Kick off the main program
fn main() {
    if env::var("RUST_LOG").is_err() {
        env::set_var("RUST_LOG", "info");
    }

    pretty_env_logger::init();

    let args = ProgArgs::parse();
    let mut object_starters = fileio::read_file(args.input);

    log::info!("Found {:?} new objects within our grid", object_starters.len());
    for object in object_starters.iter() {
        log::debug!(
            "Found new object at starting position {:?} and moving on vector {:?}",
            object.start_loc, object.vector);
    }

    let value = iterate::iterate_motion(
        &mut object_starters,
        args.max_width,
        args.max_height,
        args.iterations,
    );

    log::debug!("After {:?} iterations, the objects are now at the following position:", args.iterations);
    for y_idx in 0..args.max_height {
        let mut board_line: String = "".to_owned();
        for x_idx in 0..args.max_width {
            let count = object_starters.iter().rfold(0, |mut acc, obj| {
                if (obj.current_log == NumberPair{x: x_idx, y: y_idx}) {
                    acc += 1;
                }
                acc
            });

            if count > 0 {
                board_line.push_str(&*count.to_string());
            } else {
                board_line.push_str(".");
            }
        }
        log::debug!("{board_line:?}");
    }

    log::info!("The total value after movement is {value:?}");
}
