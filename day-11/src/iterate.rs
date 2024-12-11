use std::collections::HashMap;

/// Number iterations is a mapping block for a cache, where the number is the starting number,
/// and the iterations is the number of iterations remaining. If we get a cache hit, then the
/// value within the cache is the number of Numbers that will be generated after that number
/// of iterations.
#[derive(Hash, Eq, PartialEq, Debug)]
struct NumberIterations {
    number: u64,
    iterations: i32,
}

/// For a single number, find out the number of splits it will result in over a given period of
/// iterations. A cache is also provided for re-use throughout the application when we see similar
/// instances of a number.
fn iterate_number(cache: &mut HashMap<NumberIterations, u64>, number: u64, iteration_count: i32) -> u64 {
    let mut found_numbers: u64 = 0;

    log::debug!("Testing cache for {number:?} with {iteration_count:?} iterations remaining...");

    // If the cache contains this number for the number of iterations we're looking up...
    let curr_iteration = NumberIterations { number, iterations: iteration_count };
    if cache.contains_key(&curr_iteration) {
        // Then we can finish early with a cache hit
        let cache_get = *cache.get(&curr_iteration).unwrap();
        log::debug!("Cache hit for {number:?} with {iteration_count:?} iterations remaining -> {cache_get:?}");
        return cache_get
    }

    // If we still have iterations left to go...
    if iteration_count > 0 {
        // Perform the iteration by recursing with the changed number and a decreased iteration
        // count.
        if number.eq(&0) {
            found_numbers += iterate_number(cache, 1, iteration_count - 1);
        } else if number.to_string().len() % 2 == 0 {
            let stringified = number.to_string();
            let (first, last) = stringified.split_at(stringified.len() / 2);
            found_numbers += iterate_number(cache, first.parse::<u64>().unwrap(), iteration_count - 1);
            found_numbers += iterate_number(cache, last.parse::<u64>().unwrap(), iteration_count - 1);
        } else {
            found_numbers += iterate_number(cache, number * 2024, iteration_count - 1);
        }
    } else {
        // This is the final case where we have reached the end and no further iterations can take
        // place.
        found_numbers = 1
    }

    // Add the result to the cache for future proofing.
    log::debug!("Cache miss for {number:?} with {iteration_count:?} iterations remaining -> {found_numbers:?}");
    cache.insert(NumberIterations{number, iterations: iteration_count}, found_numbers);

    found_numbers
}

/// Kick off the iterations with a depth-first approach. Go through each individual number to
/// fill out the cache as required and slowly add up the total number of numbers found.
pub(crate) fn iterate_vector(number_vector: &Vec<u64>, iteration_count: i32) -> u64 {
    let mut cache: HashMap<NumberIterations, u64> = HashMap::new();

    let mut found_number = 0;
    for e in number_vector.iter() {
        found_number += iterate_number(&mut cache, *e, iteration_count)
    }

    found_number
}