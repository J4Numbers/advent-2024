#[derive(Copy, Clone, Hash, Eq, PartialEq, Debug)]
pub struct NumberPair {
    pub x: i32,
    pub y: i32,
}

#[derive(Copy, Clone, Hash, Eq, PartialEq, Debug)]
pub struct ObjectInMotion {
    pub start_loc: NumberPair,
    pub current_log: NumberPair,
    pub vector: NumberPair,
}
