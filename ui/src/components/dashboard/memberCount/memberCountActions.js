import * as dashboardActionTypes from "../DashboardActionTypes";
import * as dashboardApi from "../../../api/dashboardApi";
import {
  beginApiCall,
  apiCallError
} from "../../../redux/actions/apiStatusActions";

export function loadMemberCountSuccess(dashboardMemberCount) {
  return {
    type: dashboardActionTypes.LOAD_MEMBER_COUNT_SUCCESS,
    dashboardMemberCount
  };
}

export function loadMemberCount() {
  return function(dispatch) {
    dispatch(beginApiCall());
    return dashboardApi
      .getActiveInActiveMembers()
      .then(dashboardMemberCount => {
        dispatch(loadMemberCountSuccess(dashboardMemberCount));
      })
      .catch(error => {
        dispatch(apiCallError(error));
        throw error;
      });
  };
}
