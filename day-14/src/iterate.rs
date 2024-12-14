use crate::objects::{NumberPair, ObjectInMotion};

fn iterate_object(object: &mut ObjectInMotion, max_width: i32, max_height: i32) {
    let mut new_loc = NumberPair{
        x: (object.current_log.x + object.vector.x) % max_width,
        y: (object.current_log.y + object.vector.y) % max_height,
    };

    if new_loc.x < 0 {
        new_loc.x = max_width + new_loc.x;
    }
    if new_loc.y < 0 {
        new_loc.y = max_height + new_loc.y;
    }

    object.current_log = new_loc;
}

fn calc_quadrant(object_list: &Vec<ObjectInMotion>, lesser_bound: NumberPair, upper_bound: NumberPair) -> i64 {
    let mut count = 0;
    for obj in object_list.iter() {
        if obj.current_log.x >= lesser_bound.x && obj.current_log.x < upper_bound.x &&
            obj.current_log.y >= lesser_bound.y && obj.current_log.y < upper_bound.y {
            count += 1;
        }
    }
    count
}

fn calc_quadrants(object_list: &Vec<ObjectInMotion>, iter_count: i32, max_width: i32, max_height: i32) -> i64 {
    let quad_ul = calc_quadrant(
        object_list,
        NumberPair{x: 0, y: 0},
        NumberPair{x: max_width / 2, y: max_height / 2},
    );
    let quad_ur = calc_quadrant(
        object_list,
        NumberPair{x: max_width - (max_width / 2), y: 0},
        NumberPair{x: max_width, y: max_height / 2},
    );
    let quad_dl = calc_quadrant(
        object_list,
        NumberPair{x: 0, y: max_height - (max_height / 2)},
        NumberPair{x: max_width / 2, y: max_height},
    );
    let quad_dr = calc_quadrant(
        object_list,
        NumberPair{x: max_width - (max_width / 2), y: max_height - (max_height / 2)},
        NumberPair{x: max_width, y: max_height},
    );

    let stats_total = quad_ul * quad_ur * quad_dl * quad_dr;

    if stats_total < 100000000 {
        log::debug!("{iter_count:?} :: {quad_ul:?} * {quad_ur:?} * {quad_dl:?} * {quad_dr:?} = {:?}",
            stats_total);
    }

    stats_total
}

pub(crate) fn iterate_motion(object_list: &mut Vec<ObjectInMotion>, max_width: i32, max_height: i32, iterations: i32) -> i64 {
    let mut remaining_iterations = 0;

    while remaining_iterations < iterations {
        for obj in object_list.iter_mut() {
            iterate_object(obj, max_width, max_height);
        }

        remaining_iterations += 1;
        calc_quadrants(object_list, remaining_iterations, max_width, max_height);
    }

    calc_quadrants(object_list, iterations, max_width, max_height)
}
