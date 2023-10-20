import React, { useEffect, useState } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { loadSat } from "./socialActivityTotalsAction";

const SocialActivityTotals = props => {
  const { sat, error, loading, loadSat } = props;

  useEffect(() => {
    loadSat();
  }, []);

  const [selectedTab, setSelectedTab] = useState("YTD");
  const [selectedRec, setSelectedRec] = useState("");

  if (selectedRec === "" && sat.length > 0) {
    updateSelectedTab(null, "YTD");
  }

  function updateSelectedTab(e, value) {
    if (e !== undefined && e !== null) {
      e.stopPropagation();
      e.preventDefault();
    }

    setSelectedTab(value);

    sat.map(rec => {
      if (rec.totalType === value) {
        setSelectedRec(rec);
      }
    });
  }

  if (selectedRec === "") {
    return "";
  }

  return (
    <div className="item col-xs-12 col-md-4">
      <div className="panel panel-default">
        <div className="panel-body">
          <div className="btn-group btn-group-justified">
            <a
              href="#"
              onClick={e => updateSelectedTab(e, "YTD")}
              className={
                selectedTab === "YTD" ? "btn btn-green" : "btn btn-white"
              }
            >
              YTD
            </a>
            <a
              href="#"
              onClick={e => updateSelectedTab(e, "Month")}
              className={
                selectedTab === "Month" ? "btn btn-green" : "btn btn-white"
              }
            >
              Month
            </a>
            <a
              href="#"
              onClick={e => updateSelectedTab(e, "Quarter")}
              className={
                selectedTab === "Quarter" ? "btn btn-green" : "btn btn-white"
              }
            >
              Quarter
            </a>
            <a
              href="#"
              onClick={e => updateSelectedTab(e, "Year")}
              className={
                selectedTab === "Year" ? "btn btn-green" : "btn btn-white"
              }
            >
              Year
            </a>
          </div>
        </div>
        <hr className="margin-none" />

        <ul className="list-group">
          <li className="list-group-item">
            <div className="media v-middle">
              <div className="media-left" />
              <div className="media-body">Total Members</div>
              <div className="media-right">
                <div className="text-subhead">{selectedRec.totalMembers}</div>
              </div>
            </div>
          </li>
          <li className="list-group-item">
            <div className="media v-middle">
              <div className="media-left" />
              <div className="media-body">Total posts</div>
              <div className="media-right">
                <div className="text-subhead">{selectedRec.totalPosts}</div>
              </div>
            </div>
          </li>
          <li className="list-group-item">
            <div className="media v-middle">
              <div className="media-left" />
              <div className="media-body">Interactions</div>
              <div className="media-right">
                <div className="text-subhead">
                  {selectedRec.totalInteractions}
                </div>
              </div>
            </div>
          </li>
          <li className="list-group-item">
            <div className="media v-middle">
              <div className="media-left" />
              <div className="media-body">Interactions Rate</div>
              <div className="media-right">
                <div className="text-subhead">
                  {selectedRec.totalInteractions / selectedRec.totalPosts}
                </div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  );
};

SocialActivityTotals.propTypes = {
  sat: PropTypes.array.isRequired,
  loadSat: PropTypes.func.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string
};

function mapStateToProps(state) {
  const loading = state.apiCallsInProgress > 0;
  const { error } = state;
  const { sat } = state;
  return {
    loading,
    error,
    sat
  };
}

const mapDispatchToProps = {
  loadSat
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SocialActivityTotals);
