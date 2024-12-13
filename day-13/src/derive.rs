use crate::objects::{Equation, ValuePair};

/// Use Cramer's rule to find out the lowest unique solution to two linear equations.
fn cramer_rule(equation: &Equation) -> Option<ValuePair> {
    let det_coefficient = (equation.a.x * equation.b.y) - (equation.a.y * equation.b.x);
    let det_x = (equation.prize.x * equation.b.y) - (equation.prize.y * equation.b.x);
    let det_y = (equation.a.x * equation.prize.y) - (equation.a.y * equation.prize.x);

    let cramer_a = det_x / det_coefficient;
    let cramer_b = det_y / det_coefficient;

    let actual_x = (cramer_a * equation.a.x) + (cramer_b * equation.b.x);
    let actual_y = (cramer_a * equation.a.y) + (cramer_b * equation.b.y);

    let mut cramer_ret: Option<ValuePair> = None;

    // Double check that the answer we've been given is actually accurate...
    if actual_x == equation.prize.x && actual_y == equation.prize.y {
        log::debug!(
            "Found valid click pair for X prize {:?} - {:?}*{:?} + {:?}*{:?}",
            equation.prize.x, cramer_a, equation.a.x, cramer_b, equation.b.x,
        );
        log::debug!(
            "Found valid click pair for Y prize {:?} - {:?}*{:?} + {:?}*{:?} -> {:?} tokens",
            equation.prize.y, cramer_a, equation.a.y, cramer_b, equation.b.y, (cramer_a * 3) + cramer_b,
        );
        cramer_ret = Some(ValuePair{x: cramer_a, y: cramer_b});
    } else {
        log::debug!("No valid pairing found for equation.");
    }

    cramer_ret
}

/// Return the score value for a solution according to the rule that all presses of the A button
/// cost 3 tokens, and 1 token per B-button press.
fn score_solutions(solutions: &Vec<Option<ValuePair>>) -> i64 {
    let mut total_score = 0;

    for sol in solutions.iter() {
        if !sol.is_none() {
            total_score += sol.unwrap().x * 3;
            total_score += sol.unwrap().y;
        }
    }

    total_score
}

/// Return the total number of tokens required to win all possible games provided to this function.
pub(crate) fn derive_solutions(equations: &Vec<Equation>) -> i64 {
    let mut solutions: Vec<Option<ValuePair>> = vec![];

    for equat in equations.iter() {
        solutions.push(cramer_rule(equat));
    }

    score_solutions(&solutions)
}