export default function reducer(state={
    refreshPeriodMillis: 1000,
    users: [],
}, action) {

    switch (action.type) {
        case "REFRESH_USERS": {
            console.log("value: " + action.value);
            return {...state, users: action.value}
        }
        case "SET_REFRESH_PERIOD": {
            return {...state, refreshPeriodMillis: action.value}
        }
    }

    return state
}