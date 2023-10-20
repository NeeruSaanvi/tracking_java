import * as dashboardActionTypes from "../DashboardActionTypes";
import * as dashboardApi from "../../../api/dashboardApi";
import {
  beginApiCall,
  apiCallError
} from "../../../redux/actions/apiStatusActions";

export function loadMostUsedKeywordsSuccess(dashboardMostUsedKeywords) {
  console.log("data:: " + dashboardMostUsedKeywords);
  return {
    type: dashboardActionTypes.LOAD_MOST_USED_KEYWORDS_SUCCESS,
    dashboardMostUsedKeywords
  };
}

export function loadMostUsedKeywords() {
  return function(dispatch) {
    dispatch(beginApiCall());
    return dashboardApi
      .getMostUsedKeywords()
      .then(dashboardMostUsedKeywords => {
        dispatch(loadMostUsedKeywordsSuccess(dashboardMostUsedKeywords.data));
      })
      .catch(error => {
        dispatch(apiCallError(error));
        throw error;
      });
  };
}
