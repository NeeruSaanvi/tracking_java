import React, { useEffect } from "react";
import Header from "../common/Header";
import Footer from "../common/Footer";
import Sidebar from "../common/Sidebar";
import refreshableFetch from "../../resources/refreshableFetch";
import moment from "moment";
import WebPostGrid from "./WebPostGrid";

function WebPostApp() {
  useEffect(() => {
    refreshableFetch(null, null);
  }, []);
  return (
    <div className="st-container">
      <Header role="navigation" />
      <Sidebar />
      <div className="st-content" id="content">
        <div className="st-content-inner">
          <div className="container-fluid">
            <div className="page-section">
              <div className="row">
                <WebPostGrid />
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer year={"" + moment().year()} />
    </div>
  );
}

export default WebPostApp;
