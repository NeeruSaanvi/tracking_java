import * as types from "../actions/actionTypes";
import initialState from "./initialState";

export default function apiStatusErrorReducer(
  state = initialState.error,
  action
) {
  if (action.type === types.API_CALL_ERROR) {
    return action.error.message;
  }

  return state;
}
