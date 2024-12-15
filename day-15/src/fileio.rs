use crate::objects::{FileReport, Box, NumberPair};

use std::io;
use std::fs::File;
use std::io::BufRead;
use std::path::Path;
use regex::Regex;

/// Read a given file in according some basic rules, splitting it into different lines
/// as long as each line matches a given regex pattern.
///
/// # Arguments
///
/// * `file_loc` - The path location to the file that we're reading in
pub(crate) fn read_file<'h, P: std::convert::AsRef<Path>>(file_loc: P, scale: bool) -> FileReport {
    let mut file_report = FileReport{
        grid: vec![],
        box_locs: vec![],
        robot_loc: NumberPair{x: -1, y: -1},
        instructions: vec![],
    };
    let map_rule = Regex::new(r"([#.O@])").unwrap();
    let instruction_rule = Regex::new(r"([\^<>v])").unwrap();

    let mut y_idx = 0;

    if let Ok(lines) = read_lines(file_loc) {
        for test_line in lines {
            if let Ok(line) = test_line {
                if map_rule.is_match(&*line) {
                    let mut ret_line: Vec<char> = vec![];
                    let mut x_idx = 0;
                    for (_, [valid_line]) in map_rule.captures_iter(&*line).map(|c| c.extract()) {
                        let mut char_to_store = String::from(valid_line).chars().last().unwrap();
                        if valid_line == "@" {
                            file_report.robot_loc = NumberPair{x: x_idx, y: y_idx};
                            char_to_store = '.';
                        }

                        if valid_line == "O" {
                            file_report.box_locs.push(Box{
                                left_loc: NumberPair{x: x_idx, y: y_idx},
                                moved: false,
                                width: if scale { 2 } else { 1 },
                            });
                            char_to_store = '.';
                        }

                        if scale {
                            ret_line.push(char_to_store);
                            x_idx += 1;
                        }

                        ret_line.push(char_to_store);
                        x_idx += 1;
                    }
                    file_report.grid.push(ret_line);
                    y_idx += 1;
                }

                if instruction_rule.is_match(&*line) {
                    for (_, [instr]) in instruction_rule.captures_iter(&*line).map(|c| c.extract()) {
                        let num_inst: NumberPair;
                        if instr == "^" {
                            num_inst = NumberPair{x: 0, y: -1};
                        } else if instr == "<" {
                            num_inst = NumberPair{x: -1, y: 0};
                        } else if instr == ">" {
                            num_inst = NumberPair{x: 1, y: 0};
                        } else {
                            num_inst = NumberPair{x: 0, y: 1};
                        }
                        file_report.instructions.push(num_inst);
                    }
                }
            }
        }
    }
    file_report
}

/// Scan in all the lines of a given file, splitting them apart on newline and returning the
/// whole file, even if not all of the lines are valid.
///
/// # Arguments
///
/// * `file_loc` - The path location to the file we're reading in
fn read_lines<P>(file_loc: P) -> io::Result<io::Lines<io::BufReader<File>>> where P: AsRef<Path>, {
    let file = File::open(file_loc)?;
    Ok(io::BufReader::new(file).lines())
}
