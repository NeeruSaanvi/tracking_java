import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { handleResponse, handleError, getHeaders } from "../../../api/apiUtils";
import * as $ from "jquery";
import bootstrap from "bootstrap";
import select2 from "select2";
const baseUrl = process.env.API_URL;

const Manufacturer = props => {
  const [data, setData] = useState([]);

  useEffect(() => {
    $("#manufactureSelect").select2();
    loadData();
  }, []);

  function loadData() {
    /*fetch(baseUrl + "/web/deleteWebPost?blogId=", {
      method: "GET",
      headers: getHeaders()
    })
      .then(handleResponse)
      .then(res => {
        setData(res.data);
      })
      .catch(handleError);*/
  }
  return (
    <div className="form-group">
      <select
        multiple="multiple"
        style={{ width: 500 }}
        data-toggle="select2"
        id="manufactureSelect"
      >
        <optgroup label="Alaskan/Hawaiian Time Zone">
          <option value="AK">Alaska</option>
          <option value="HI">Hawaii</option>
        </optgroup>
        <optgroup label="Pacific Time Zone">
          <option value="CA">California</option>
          <option value="NV">Nevada</option>
          <option value="OR">Oregon</option>
          <option value="WA">Washington</option>
        </optgroup>
        <optgroup label="Mountain Time Zone">
          <option value="AZ">Arizona</option>
          <option value="CO">Colorado</option>
          <option value="ID">Idaho</option>
          <option value="MT">Montana</option>
          <option value="NE">Nebraska</option>
          <option value="NM">New Mexico</option>
          <option value="ND">North Dakota</option>
          <option value="UT">Utah</option>
          <option value="WY">Wyoming</option>
        </optgroup>
        <optgroup label="Central Time Zone">
          <option value="AL">Alabama</option>
          <option value="AR">Arkansas</option>
          <option value="IL">Illinois</option>
          <option value="IA">Iowa</option>
          <option value="KS">Kansas</option>
          <option value="KY">Kentucky</option>
          <option value="LA">Louisiana</option>
          <option value="MN">Minnesota</option>
          <option value="MS">Mississippi</option>
          <option value="MO">Missouri</option>
          <option value="OK">Oklahoma</option>
          <option value="SD">South Dakota</option>
          <option value="TX">Texas</option>
          <option value="TN">Tennessee</option>
          <option value="WI">Wisconsin</option>
        </optgroup>
        <optgroup label="Eastern Time Zone">
          <option value="CT">Connecticut</option>
          <option value="DE">Delaware</option>
          <option value="FL">Florida</option>
          <option value="GA">Georgia</option>
          <option value="IN">Indiana</option>
          <option value="ME">Maine</option>
          <option value="MD">Maryland</option>
          <option value="MA">Massachusetts</option>
          <option value="MI">Michigan</option>
          <option value="NH">New Hampshire</option>
          <option value="NJ">New Jersey</option>
          <option value="NY">New York</option>
          <option value="NC">North Carolina</option>
          <option value="OH">Ohio</option>
          <option value="PA">Pennsylvania</option>
          <option value="RI">Rhode Island</option>
          <option value="SC">South Carolina</option>
          <option value="VT">Vermont</option>
          <option value="VA">Virginia</option>
          <option value="WV">West Virginia</option>
        </optgroup>
      </select>
    </div>
  );
};

export default Manufacturer;
