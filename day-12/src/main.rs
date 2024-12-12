extern crate pretty_env_logger;

mod fileio;
mod region;

use std::env;
use clap::Parser;

#[derive(Parser, Debug)]
#[command(author, version, about, long_about = None)]
struct ProgArgs {
    #[arg(short, long)]
    input: String,
}

/// Kick off the main program
fn main() {
    if env::var("RUST_LOG").is_err() {
        env::set_var("RUST_LOG", "info");
    }

    pretty_env_logger::init();

    let args = ProgArgs::parse();
    let char_map = fileio::read_file(args.input);

    log::debug!("Initial file was {char_map:?}");

    let total_value = region::explore_regions(&char_map);

    log::info!("Total value of the map was {total_value:?}");
}