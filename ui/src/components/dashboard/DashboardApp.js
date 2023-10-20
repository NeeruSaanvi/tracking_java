import React, { useEffect } from "react";
import Header from "../common/Header";
import Footer from "../common/Footer";
import Sidebar from "../common/Sidebar";
import Sidebar1 from "../common/Sidebar1";
import MemberCount from "./memberCount/MemberCount";
import refreshableFetch from "../../resources/refreshableFetch";
import moment from "moment";
import MostUsedKeywords from "./mostUsedKeyword/MostUsedKeywords";
import onClickForTimeout from "../../resources/sesionTimeout";
import SocialActivityTotals from "./socialActivityTotals/SocialActivityTotals";
import TestGrid from "./testGrid/TestGrid";

function DashboardApp() {
  useEffect(() => {
    refreshableFetch(null, null);

    //Onclick event for entire document which resets the session timer anytime user clicks anywhere in the page
    document.onclick = onClickForTimeout;
    //Start the timer for the session on page load
    onClickForTimeout();
  }, []);

  return (
    <div className="st-container" style={{ height: "800px" }}>
      <Header role="navigation" />
      <Sidebar1 />
      <div className="st-pusher">
        <Sidebar />
        <div className="st-content" id="content">
          <div className="st-content-inner">
            <div className="container-fluid">
              <MemberCount />
              <MostUsedKeywords />
              <SocialActivityTotals />
              <TestGrid />
            </div>
          </div>
        </div>
        <Footer year={"" + moment().year()} />
      </div>
    </div>
  );
}

export default DashboardApp;
