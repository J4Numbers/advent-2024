use crate::objects::{NumberPair, ObjectInMotion};

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
pub(crate) fn read_file<'h, P: std::convert::AsRef<Path>>(file_loc: P) -> Vec<ObjectInMotion> {
    let mut ret_map = vec![];
    let object_rule = Regex::new(r"^p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)$").unwrap();

    if let Ok(lines) = read_lines(file_loc) {
        for test_line in lines {
            if let Ok(line) = test_line {
                if object_rule.is_match(&*line) {
                    let caps = object_rule.captures(&*line).unwrap();
                    ret_map.push(ObjectInMotion{
                        start_loc: NumberPair{
                            x: (&caps[1]).parse::<i32>().unwrap(),
                            y: (&caps[2]).parse::<i32>().unwrap(),
                        },
                        current_log: NumberPair{
                            x: (&caps[1]).parse::<i32>().unwrap(),
                            y: (&caps[2]).parse::<i32>().unwrap(),
                        },
                        vector: NumberPair{
                            x: (&caps[3]).parse::<i32>().unwrap(),
                            y: (&caps[4]).parse::<i32>().unwrap(),
                        },
                    });
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
