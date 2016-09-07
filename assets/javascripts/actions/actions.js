export function setPickFromMode() {
    return {
        type: "PICK_FROM",
    }
}

export function setPickToMode() {
    return {
        type: 'PICK_TO',
    }
}

export function fromPointSelected(latlng) {
    return {
        type: 'FROM_POINT_SELECTED',
        latlng: latlng,
    }
}

export function toPointSelected(latlng) {
    return {
        type: 'TO_POINT_SELECTED',
        latlng: latlng,
    }
}

// in time of submit or discard
// maybe it's not necessary
export function setPickNoneMode() {
    return {
        type: 'PICK_NONE',
    }
}