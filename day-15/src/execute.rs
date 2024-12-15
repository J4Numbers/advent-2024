use crate::objects::{FileReport, Box, NumberPair};

fn test_can_move(piece_to_move: &NumberPair, move_to_attempt: NumberPair, box_locs: &Vec<Box>, grid: &Vec<Vec<char>>) -> bool {
    let mut piece = '@';
    for box_test in box_locs.iter() {
        let r_approach = NumberPair{x: piece_to_move.x - 1, y: piece_to_move.y};
        if box_test.width == 1 && box_test.left_loc == *piece_to_move {
            piece = 'O';
        }
        if box_test.width == 2 && box_test.left_loc == *piece_to_move {
            piece = '[';
        }
        if box_test.width == 2 && box_test.left_loc == r_approach {
            piece = ']';
        }
    }

    let test_move = NumberPair{
        x: piece_to_move.x + move_to_attempt.x,
        y: piece_to_move.y + move_to_attempt.y
    };

    let grid_check = grid.get(test_move.y as usize).unwrap().get(test_move.x as usize).unwrap();
    let mut can_move = !grid_check.eq(&'#');

    log::debug!("Testing {piece_to_move:?} ({piece:?}) can move {move_to_attempt:?} ({grid_check:?}) resulted in {can_move:?}");

    // Width-one case
    if box_locs.contains(&Box{left_loc: test_move, moved: false, width: 1}) {
        can_move = can_move && test_can_move(&test_move, move_to_attempt, box_locs, grid);
    }

    // Width-two case for horizontal movement
    if move_to_attempt.y == 0 {
        // Width-two case hitting the left side
        if box_locs.contains(&Box{left_loc: test_move, moved: false, width: 2}) {
            can_move = can_move && test_can_move(&NumberPair{x: test_move.x, y: test_move.y}, move_to_attempt, box_locs, grid);
        }

        // Width-two case hitting the right side
        if box_locs.contains(&Box{left_loc: NumberPair{x: test_move.x - 1, y: test_move.y}, moved: false, width: 2}) {
            can_move = can_move && test_can_move(&NumberPair{x: test_move.x, y: test_move.y}, move_to_attempt, box_locs, grid);
        }
    }

    // Width-two case for vertical movement
    if move_to_attempt.x == 0 {
        // Vertical hitting the left side
        if box_locs.contains(&Box{left_loc: test_move, moved: false, width: 2}) {
            can_move = can_move && test_can_move(&test_move, move_to_attempt, box_locs, grid)
                && test_can_move(&NumberPair{x: test_move.x + 1, y: test_move.y}, move_to_attempt, box_locs, grid);
        }

        // Vertical hitting the right side
        if box_locs.contains(&Box{left_loc: NumberPair{x: test_move.x - 1, y: test_move.y}, moved: false, width: 2}) {
            can_move = can_move && test_can_move(&NumberPair{x: test_move.x - 1, y: test_move.y}, move_to_attempt, box_locs, grid)
                && test_can_move(&test_move, move_to_attempt, box_locs, grid);
        }
    }

    can_move
}

fn attempt_move(piece_to_move: &NumberPair, move_to_attempt: NumberPair, box_locs: &mut Vec<Box>, grid: &Vec<Vec<char>>) -> NumberPair {
    let moved_piece;

    if test_can_move(piece_to_move, move_to_attempt, box_locs, grid) {
        moved_piece = NumberPair{
            x: piece_to_move.x + move_to_attempt.x,
            y: piece_to_move.y + move_to_attempt.y
        };

        let mut pieces_to_move = vec![moved_piece];
        let mut move_piece = pieces_to_move.pop();

        while move_piece.is_some() {
            let test_o_box = Box{left_loc: move_piece.unwrap(), moved: false, width: 1};
            let test_l_box = Box{left_loc: move_piece.unwrap(), moved: false, width: 2};
            let test_r_box = Box{left_loc: NumberPair{x: move_piece.unwrap().x - 1, y: move_piece.unwrap().y}, moved: false, width: 2};
            log::debug!("Searching for boxes in position {:?}", move_piece.unwrap());
            for box_loc in box_locs.iter_mut() {
                // Width-one box logic
                if box_loc == &test_o_box {
                    let updated_box = NumberPair {
                        x: box_loc.left_loc.x + move_to_attempt.x,
                        y: box_loc.left_loc.y + move_to_attempt.y,
                    };
                    log::debug!(
                        "Found box in position {:?}! Searching for box in pushed position {:?}",
                        move_piece.unwrap(), updated_box);
                    pieces_to_move.push(updated_box);
                    box_loc.left_loc.x += move_to_attempt.x;
                    box_loc.left_loc.y += move_to_attempt.y;
                    box_loc.moved = true;
                }

                // Width-two box logic
                if box_loc == &test_l_box {
                    // Vertical movement
                    if move_to_attempt.x == 0 {
                        let l_diff = NumberPair {
                            x: box_loc.left_loc.x,
                            y: box_loc.left_loc.y + move_to_attempt.y
                        };
                        let r_diff = NumberPair {
                            x: box_loc.left_loc.x + 1,
                            y: box_loc.left_loc.y + move_to_attempt.y
                        };
                        pieces_to_move.push(l_diff);
                        pieces_to_move.push(r_diff);
                        box_loc.left_loc.y += move_to_attempt.y;
                        box_loc.moved = true;
                    } else {
                        let diff = NumberPair {
                            x: box_loc.left_loc.x + (move_to_attempt.x * box_loc.width as i32),
                            y: box_loc.left_loc.y,
                        };
                        pieces_to_move.push(diff);
                        box_loc.left_loc.x += move_to_attempt.x;
                        box_loc.moved = true;
                    }
                }

                if box_loc == &test_r_box {
                    // Vertical movement
                    if move_to_attempt.x == 0 {
                        let l_diff = NumberPair {
                            x: test_r_box.left_loc.x,
                            y: test_r_box.left_loc.y + move_to_attempt.y
                        };
                        let r_diff = NumberPair {
                            x: test_r_box.left_loc.x + 1,
                            y: test_r_box.left_loc.y + move_to_attempt.y
                        };
                        pieces_to_move.push(l_diff);
                        pieces_to_move.push(r_diff);
                        box_loc.left_loc.y += move_to_attempt.y;
                        box_loc.moved = true;
                    } else {
                        let diff = NumberPair {
                            x: box_loc.left_loc.x + (move_to_attempt.x),
                            y: box_loc.left_loc.y,
                        };
                        pieces_to_move.push(diff);
                        box_loc.left_loc.x += move_to_attempt.x;
                        box_loc.moved = true;
                    }
                }
            }
            move_piece = pieces_to_move.pop();
        }
    } else {
        moved_piece = *piece_to_move;
    }
    moved_piece
}

fn score_objects(box_locs: &Vec<Box>) -> i64 {
    let mut score: i64 = 0;

    for box_loc in box_locs.iter() {
        score += ((box_loc.left_loc.y * 100) + box_loc.left_loc.x) as i64;
    }

    score
}

pub(crate) fn execute_instructions(file_report: &mut FileReport) -> i64 {
    for instr in file_report.instructions.iter_mut() {
        file_report.robot_loc = attempt_move(
            &file_report.robot_loc,
            *instr,
            &mut file_report.box_locs,
            &file_report.grid
        );

        for box_loc in file_report.box_locs.iter_mut() {
            box_loc.moved = false;
        }
        // for y_idx in 0..file_report.grid.len() {
        //     let mut board_line: String = "".to_owned();
        //     for x_idx in 0..file_report.grid.get(0).unwrap().len() {
        //         let mut char_to_print = file_report.grid.get(y_idx).unwrap().get(x_idx).unwrap().to_string();
        //         let test_pair = NumberPair{x: x_idx as i32, y: y_idx as i32};
        //         let test_l_box = Box{left_loc: test_pair, moved: false, width: 2};
        //         let test_r_box = Box{left_loc: NumberPair{x: x_idx as i32 - 1, y: y_idx as i32}, moved: false, width: 2};
        //         let test_o_box = Box{left_loc: test_pair, moved: false, width: 1};
        //
        //         if file_report.box_locs.contains(&test_o_box) {
        //             char_to_print = "O".parse().unwrap();
        //         }
        //         if file_report.box_locs.contains(&test_l_box) {
        //             char_to_print = "[".parse().unwrap();
        //         }
        //         if file_report.box_locs.contains(&test_r_box) {
        //             char_to_print = "]".parse().unwrap();
        //         }
        //         if test_pair == file_report.robot_loc {
        //             char_to_print = "@".parse().unwrap();
        //         }
        //
        //         board_line.push_str(&*char_to_print);
        //     }
        //     log::debug!("{board_line:?}");
        // }
    }

    score_objects(&file_report.box_locs)
}
