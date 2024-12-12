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
pub(crate) fn read_file<'h, P: std::convert::AsRef<Path>>(file_loc: P) -> Vec<Vec<char>> {
    let mut ret_map = vec![];
    let re = Regex::new(r"([A-Z])").unwrap();
    if let Ok(lines) = read_lines(file_loc) {
        for test_line in lines {
            if  let Ok(line) = test_line {
                let mut ret_line: Vec<char> = vec![];
                for (_, [valid_line]) in re.captures_iter(&*line).map(|c| c.extract()) {
                    ret_line.push(String::from(valid_line).chars().last().unwrap())
                }
                ret_map.push(ret_line)
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