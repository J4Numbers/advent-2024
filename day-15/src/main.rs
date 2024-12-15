extern crate pretty_env_logger;

mod objects;
mod fileio;
mod execute;

use std::env;
use std::fmt::Debug;
use clap::Parser;
use crate::objects::{FileReport, Box, NumberPair};

#[derive(Parser, Debug)]
#[command(author, version, about, long_about = None)]
struct ProgArgs {
    #[arg(short, long)]
    input: String,

    #[arg(long, default_value_t = false)]
    scale: bool,
}

fn print_grid(file_report: &FileReport) {
    for y_idx in 0..file_report.grid.len() {
        let mut board_line: String = "".to_owned();
        for x_idx in 0..file_report.grid.get(0).unwrap().len() {
            let mut char_to_print = file_report.grid.get(y_idx).unwrap().get(x_idx).unwrap().to_string();
            let test_pair = NumberPair{x: x_idx as i32, y: y_idx as i32};
            let test_l_box = Box{left_loc: test_pair, moved: false, width: 2};
            let test_r_box = Box{left_loc: NumberPair{x: x_idx as i32 - 1, y: y_idx as i32}, moved: false, width: 2};
            let test_o_box = Box{left_loc: test_pair, moved: false, width: 1};

            if file_report.box_locs.contains(&test_o_box) {
                char_to_print = "O".parse().unwrap();
            }
            if file_report.box_locs.contains(&test_l_box) {
                char_to_print = "[".parse().unwrap();
            }
            if file_report.box_locs.contains(&test_r_box) {
                char_to_print = "]".parse().unwrap();
            }
            if test_pair == file_report.robot_loc {
                char_to_print = "@".parse().unwrap();
            }

            board_line.push_str(&*char_to_print);
        }
        log::debug!("{board_line:?}");
    }
}

/// Kick off the main program
fn main() {
    if env::var("RUST_LOG").is_err() {
        env::set_var("RUST_LOG", "info");
    }

    pretty_env_logger::init();

    let args = ProgArgs::parse();
    let mut file_report = fileio::read_file(args.input, args.scale);

    log::info!(
        "Found {:?} boxes within our grid with robot starting at {:?}",
        file_report.box_locs.len(), file_report.robot_loc);

    print_grid(&file_report);
    log::debug!("Instructions to carry out :: {:?}", file_report.instructions);

    let grid_score = execute::execute_instructions(&mut file_report);

    print_grid(&file_report);

    log::info!("After following all instructions, the grid has a score of {grid_score:?}");
}
