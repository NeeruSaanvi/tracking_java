import * as memberActionTypes from "./MemberActionTypes";
import * as memberApi from "../../api/memberApi";
import {
  beginApiCall,
  apiCallError
} from "../../redux/actions/apiStatusActions";

export function loadMembersSuccess(members) {
  console.log("data:: " + members);
  return {
    type: memberActionTypes.LOAD_MEMBER_LIST_SUCCESS,
    members
  };
}

export function loadMembers() {
  return function(dispatch) {
    dispatch(beginApiCall());
    return memberApi
      .getAllMembers()
      .then(members => {
        dispatch(loadMembersSuccess(members));
      })
      .catch(error => {
        dispatch(apiCallError(error));
        throw error;
      });
  };
}
