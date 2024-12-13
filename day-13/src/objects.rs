#[derive(Copy, Clone, Hash, Eq, PartialEq, Debug)]
pub struct ValuePair {
    pub x: i64,
    pub y: i64,
}

#[derive(Copy, Clone, Hash, Eq, PartialEq, Debug)]
pub struct Equation {
    pub a: ValuePair,
    pub b: ValuePair,
    pub prize: ValuePair,
}