extern crate pretty_env_logger;

mod fileio;
mod iterate;

use std::env;
use clap::Parser;

#[derive(Parser, Debug)]
#[command(author, version, about, long_about = None)]
struct ProgArgs {
    #[arg(short, long)]
    input: String,

    #[arg(long, default_value_t = 25)]
    iterations: i32,
}

/// Kick off the main program
fn main() {
    if env::var("RUST_LOG").is_err() {
        env::set_var("RUST_LOG", "info");
    }

    pretty_env_logger::init();

    let args = ProgArgs::parse();
    let working_numbers = fileio::read_file(args.input);

    log::info!("Initial file was {working_numbers:?}");

    let total_numbers = iterate::iterate_vector(&working_numbers, args.iterations);

    log::info!("After {:?} iterations, {:?} numbers found", args.iterations, total_numbers)
}