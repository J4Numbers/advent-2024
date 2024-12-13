extern crate pretty_env_logger;

mod objects;
mod fileio;
mod derive;

use std::env;
use clap::Parser;

#[derive(Parser, Debug)]
#[command(author, version, about, long_about = None)]
struct ProgArgs {
    #[arg(short, long)]
    input: String,

    #[arg(long, default_value_t = false)]
    extend: bool
}

/// Kick off the main program
fn main() {
    if env::var("RUST_LOG").is_err() {
        env::set_var("RUST_LOG", "info");
    }

    pretty_env_logger::init();

    let args = ProgArgs::parse();
    let equation_sets = fileio::read_file(args.input, args.extend);

    for equation in equation_sets.iter() {
        log::debug!("Found new equation with the following two rules:");
        log::debug!("\t{:?}X + {:?}Y = {:?}", equation.a.x, equation.b.x, equation.prize.x);
        log::debug!("\t{:?}X + {:?}Y = {:?}", equation.a.y, equation.b.y, equation.prize.y);
    }

    log::info!("Initial file contained {:?} equations", equation_sets.len());

    let token_score = derive::derive_solutions(&equation_sets);

    log::info!("Final token score was {:?}", token_score);
}