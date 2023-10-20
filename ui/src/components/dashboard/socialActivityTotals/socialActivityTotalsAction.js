import * as dashboardActionTypes from "../DashboardActionTypes";
import * as dashboardApi from "../../../api/dashboardApi";
import {
  beginApiCall,
  apiCallError
} from "../../../redux/actions/apiStatusActions";

export function loadSatSuccess(sat) {
  return {
    type: dashboardActionTypes.LOAD_SAT_SUCCESS,
    sat
  };
}

export function loadSat() {
  return function(dispatch) {
    dispatch(beginApiCall());
    return dashboardApi
      .getSocialActivitytotals()
      .then(sat => {
        dispatch(loadSatSuccess(sat.data));
      })
      .catch(error => {
        dispatch(apiCallError(error));
        throw error;
      });
  };
}
