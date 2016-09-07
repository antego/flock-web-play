export default function reducer(state={
    fromPoint: null,
    toPoint: null,
    pickMode: "PICK_FROM",
    newOrder: null,
}, action) {

    switch (action.type) {
        case "PICK_FROM": {
            return {...state, pickMode: "PICK_FROM"}
        }
        case "PICK_TO": {
            return {...state, pickMode: "PICK_TO"}
        }
        case "FROM_POINT_PICKED": {
            return {...state, fromPoint: action.value}
        }
        case "TO_POINT_PICKED": {
            return {...state, toPoint: action.value}
        }
    }

    return state
}