import initialState from "../../../redux/reducers/initialState";
import * as dashboardActionTypes from "../DashboardActionTypes";

export default function memberCountReducer(
  state = initialState.dashboardMemberCount,
  action
) {
  switch (action.type) {
    case dashboardActionTypes.LOAD_MEMBER_COUNT_SUCCESS:
      return action.dashboardMemberCount;
    default:
      return state;
  }
}
