import initialState from "../../redux/reducers/initialState";

export default function loginReducer(state = initialState.token, action) {
  switch (action.type) {
    case "LOGIN_SUCCESS":
      return action.token;
    default:
      return state;
  }
}
