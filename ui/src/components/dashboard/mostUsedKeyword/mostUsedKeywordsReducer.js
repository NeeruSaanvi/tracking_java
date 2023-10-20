import initialState from "../../../redux/reducers/initialState";
import * as dashboardActionTypes from "../DashboardActionTypes";

export default function mostUsedKeywordsReducer(
  state = initialState.dashboardMostUsedKeywords,
  action
) {
  switch (action.type) {
    case dashboardActionTypes.LOAD_MOST_USED_KEYWORDS_SUCCESS:
      return action.dashboardMostUsedKeywords;
    default:
      return state;
  }
}
