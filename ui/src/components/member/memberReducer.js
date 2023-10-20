import initialState from "../../redux/reducers/initialState";
import * as memberActionTypes from "./MemberActionTypes";

export default function mostUsedKeywordsReducer(
  state = initialState.members,
  action
) {
  switch (action.type) {
    case memberActionTypes.LOAD_MEMBER_LIST_SUCCESS:
      return action.members;
    default:
      return state;
  }
}
