export default function reducer(state={}
, action) {

    switch (action.type) {
        case "LOG_IN": {
            return {...state, user: action.value}
        }
    }

    return state
}