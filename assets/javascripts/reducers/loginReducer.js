export default function reducer(state={}
, action) {

    switch (action.type) {
        case "REGISTER_USER": {
            return {...state, user: action.value}
        }
    }

    return state
}