import initialState from "../../../redux/reducers/initialState";
import * as dashboardActionTypes from "../DashboardActionTypes";

export default function satReducer(state = initialState.sat, action) {
  switch (action.type) {
    case dashboardActionTypes.LOAD_SAT_SUCCESS:
      return action.sat;
    default:
      return state;
  }
}
