use crate::objects::{Equation, ValuePair};

use std::io;
use std::fs::File;
use std::io::BufRead;
use std::path::Path;
use regex::Regex;

/// Helper function to generate a base equation to be filled in.
fn generate_new_equation() -> Equation {
    Equation{
        a: ValuePair{x: -1, y: -1},
        b: ValuePair{x: -1, y: -1},
        prize: ValuePair{x: -1, y: -1},
    }
}

/// Read a given file in according some basic rules, splitting it into different lines
/// as long as each line matches a given regex pattern.
///
/// # Arguments
///
/// * `file_loc` - The path location to the file that we're reading in
pub(crate) fn read_file<'h, P: std::convert::AsRef<Path>>(file_loc: P, extend: bool) -> Vec<Equation> {
    let mut ret_map = vec![];
    let button_a = Regex::new(r"^Button A: X\+([0-9]+), Y\+([0-9]+)").unwrap();
    let button_b = Regex::new(r"^Button B: X\+([0-9]+), Y\+([0-9]+)").unwrap();
    let prize = Regex::new(r"^Prize: X=([0-9]+), Y=([0-9]+)").unwrap();

    if let Ok(lines) = read_lines(file_loc) {
        let mut eq_in_prog: Equation = generate_new_equation();
        let mut caps;

        for test_line in lines {
            if let Ok(line) = test_line {
                if button_a.is_match(&*line) {
                    caps = button_a.captures(&*line).unwrap();
                    eq_in_prog = generate_new_equation();
                    eq_in_prog.a = ValuePair{
                        x: (&caps[1]).parse::<i64>().unwrap(),
                        y: (&caps[2]).parse::<i64>().unwrap(),
                    };
                } else if button_b.is_match(&*line) {
                    caps = button_b.captures(&*line).unwrap();
                    eq_in_prog.b = ValuePair{
                        x: (&caps[1]).parse::<i64>().unwrap(),
                        y: (&caps[2]).parse::<i64>().unwrap(),
                    };
                } else if prize.is_match(&*line) {
                    caps = prize.captures(&*line).unwrap();
                    eq_in_prog.prize = ValuePair{
                        x: (&caps[1]).parse::<i64>().unwrap(),
                        y: (&caps[2]).parse::<i64>().unwrap(),
                    };
                    if extend {
                        eq_in_prog.prize.x += 10000000000000;
                        eq_in_prog.prize.y += 10000000000000;
                    }
                    ret_map.push(eq_in_prog);
                }
            }
        }
    }
    ret_map
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