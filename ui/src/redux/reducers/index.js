import { combineReducers } from "redux";
import apiCallsInProgress from "./apiStatusReducer";
import error from "./apiStatusErrorReducer";
import dashboardMemberCount from "../../components/dashboard/memberCount/memberCountReducer";
import dashboardMostUsedKeywords from "../../components/dashboard/mostUsedKeyword/mostUsedKeywordsReducer";
import members from "../../components/member/memberReducer";
import sat from "../../components/dashboard/socialActivityTotals/socialActivityTotalsReducer";

const rootReducer = combineReducers({
  apiCallsInProgress,
  error,
  dashboardMemberCount,
  dashboardMostUsedKeywords,
  members,
  sat
});

export default rootReducer;
