import React, {
  useEffect,
  useState,
  forwardRef,
  useImperativeHandle
} from "react";
import PropTypes from "prop-types";
import Alert from "../../common/Alert";
import Spinner from "../../common/Spinner";
import { handleResponse, handleError, getHeaders } from "../../../api/apiUtils";
const baseUrl = process.env.API_URL;

const Grid = forwardRef((props, ref) => {
  const { loading = false, error, config } = props;

  const {
    columns,
    id = "DataTables_Table_0",
    url,
    params,
    itemsPerPageOptions = [10, 25, 50, 100],
    numberOfPagesToDisplay = 7
  } = config;

  const [page, setPage] = useState(config.initialPage ? config.initialPage : 1);
  const [size, setSize] = useState(10);
  const [sort, setSort] = useState("");
  const [direction, setDirection] = useState("asc");
  const [search, setSearch] = useState("");
  const [data, setData] = useState([]);
  const [totalCount, setTotalCount] = useState();
  const [recordStart, setRecordStart] = useState(0);
  const [recordEnd, setRecordEnd] = useState(0);
  const [recordsBanner, setRecordsBanner] = useState("No records to display");
  const [lowerNumber, setLowerNumber] = useState(0);
  const [higherNumber, setHigherNumber] = useState(0);
  const [paginationRange, setPaginationRange] = useState([]);

  useEffect(() => {
    loadData(page, size, sort, direction, search);
  }, []);

  useImperativeHandle(ref, () => ({
    reload() {
      loadData(page, size, sort, direction, search);
    }
  }));

  function loadData(page, size, sort, direction, search) {
    fetch(baseUrl + buildUrl(page, size, sort, direction, search), {
      method: "GET",
      headers: getHeaders()
    })
      .then(handleResponse)
      .then(res => {
        setData(res.data);
        setTotalCount(res.totalCount);

        setPage(page);
        setSize(size);
        setSort(sort);
        setDirection(direction);
        setSearch(search);

        if (res.data !== null && res.data.length > 0) {
          //Calculate start record and end record
          const recordStart = (page - 1) * size + 1;
          const recordEnd = (page - 1) * size + res.data.length;

          setRecordStart(recordStart);
          setRecordEnd(recordEnd);

          setRecordsBanner(
            `Showing ${recordStart} to ${recordEnd} of ${
              res.totalCount
            } entries`
          );

          //Find pagination options
          const range = Math.floor(numberOfPagesToDisplay / 2);
          const highestRange =
            Math.floor(res.totalCount / size) +
            (res.totalCount % size > 0 ? 1 : 0);
          let lowerNumber = page - range <= 0 ? 1 : page - range;
          const higherNumber =
            lowerNumber + numberOfPagesToDisplay - 1 > highestRange
              ? highestRange
              : lowerNumber + numberOfPagesToDisplay - 1;

          while (
            higherNumber - lowerNumber < numberOfPagesToDisplay - 1 &&
            lowerNumber > 1
          ) {
            lowerNumber = lowerNumber - 1;
          }

          const paginationRange = [];
          let count = lowerNumber;
          for (count = lowerNumber; count <= higherNumber; count++) {
            paginationRange.push(count);
          }
          setLowerNumber(lowerNumber);
          setHigherNumber(higherNumber);
          setPaginationRange(paginationRange);
        } else {
          setRecordsBanner("No records to display");

          setLowerNumber(0);
          setHigherNumber(0);
          setPaginationRange([]);
        }
      })
      .catch(handleError);
  }

  function buildUrl(page, size, sort, direction, search) {
    const qParams = {
      page,
      size,
      direction,
      sort,
      search,
      ...params
    };

    var esc = encodeURIComponent;
    var query = Object.keys(qParams)
      .map(k => esc(k) + "=" + esc(qParams[k]))
      .join("&");

    return url + (query !== "" ? "?" + query : "");
  }

  function updatePage(page1, e, loadDataBool) {
    e.stopPropagation();
    e.preventDefault();

    if (loadDataBool !== undefined && loadDataBool === true) {
      loadData(page1, size, sort, direction, search);
    }
  }

  function updateSize(size1) {
    loadData(page, size1, sort, direction, search);
  }

  function updateSort(sort1, e) {
    e.stopPropagation();
    e.preventDefault();
    loadData(page, size, sort1, direction, search);
  }

  function updateDirection(direction1) {
    loadData(page, size, sort, direction1, search);
  }

  function updateSearch(search1) {
    if (search1.length > 3) {
      loadData(page, size, sort, direction, search1);
    }
  }

  /*
  if (loading) {
    return <Spinner />;
  }

  if (error !== null) {
    return (
      <Alert
        show={error !== null}
        type={error !== null ? "failure" : "success"}
      >
        There was an error fetching data
      </Alert>
    );
  }
*/
  return (
    <div
      id={id + "_wrapper"}
      className="dataTables_wrapper form-inline dt-bootstrap"
    >
      <div className="row">
        <div className="col-sm-6">
          <div className="dataTables_length" id={id + "_length"}>
            <label>
              Show &nbsp;
              <select
                name={id + "_length"}
                aria-controls={id}
                className="form-control input-sm"
                onChange={e => updateSize(e.target.value)}
                //value={size}
              >
                {itemsPerPageOptions.map(option => {
                  return (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  );
                })}
              </select>
              &nbsp;entries
            </label>
          </div>
        </div>
        <div className="col-sm-6">
          <div id={id + "_filter"} className="dataTables_filter">
            <label>
              Search:
              <input
                type="search"
                className="form-control input-sm"
                placeholder=""
                aria-controls={id}
                onChange={e => updateSearch(e.target.value)}
              />
            </label>
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-sm-12">
          <table
            data-toggle="data-table"
            className="table dataTable"
            cellSpacing="0"
            width="100%"
            id={id}
            role="grid"
            aria-describedby={id + "_info"}
            style={{ width: "100%" }}
          >
            <thead>
              <tr key="thead_row" role="row">
                {columns.map(conf => {
                  return (
                    <th
                      key={conf.headerTitle}
                      className={
                        conf.headerClass ? conf.headerClass : "sorting"
                      }
                      tabIndex="0"
                      aria-controls={id}
                      rowSpan={conf.rowSpan ? conf.rowSpan : "1"}
                      colSpan={conf.colSpan ? conf.colSpan : "1"}
                      aria-sort={conf.sort ? conf.sort : ""}
                      aria-label="Name: activate to sort column descending"
                      style={
                        conf.headerStyle ? conf.headerStyle : { width: "180px" }
                      }
                      onClick={e => updateSort(conf.data, e)}
                    >
                      {conf.headerTitle}
                    </th>
                  );
                })}
              </tr>
            </thead>
            <tbody id="responsive-table-body">
              {data.map((rec, index) => {
                const trClassName = index % 2 === 0 ? "even" : "odd";
                return (
                  <tr
                    key={rec.id}
                    id={rec.id + "_tr"}
                    role="row"
                    className={trClassName}
                  >
                    {columns.map(conf => {
                      return (
                        <td
                          key={conf.id}
                          id={conf.id + "_td"}
                          className={conf.className ? conf.className : ""}
                          style={conf.style ? conf.style : {}}
                        >
                          {conf.type === "checkbox" && (
                            <div
                              className={
                                conf.divClass
                                  ? conf.divClass + "checkbox checkbox-single"
                                  : "checkbox checkbox-single"
                              }
                              style={conf.divStyle ? conf.divStye : {}}
                            >
                              <input
                                id={conf.id}
                                type="checkbox"
                                checked={rec[conf.data]}
                              />
                              <label htmlFor={conf.id}>{conf.label}</label>
                            </div>
                          )}

                          {conf.type === "text" && rec[conf.data]}

                          {conf.type === "renderer" && conf.renderer(rec)}
                        </td>
                      );
                    })}
                  </tr>
                );
              })}
            </tbody>

            <tfoot>
              <tr>
                {columns.map(conf => {
                  return (
                    <th key={conf.id + "_tfoot"} rowSpan="1" colSpan="1">
                      {conf.headerTitle}
                    </th>
                  );
                })}
              </tr>
            </tfoot>
          </table>
        </div>
        <div className="row">
          <div className="col-sm-3">
            <div
              className="dataTables_info"
              id={id + "_info"}
              role="status"
              aria-live="polite"
            >
              {recordsBanner}
            </div>
          </div>
          <div className="col-sm-9">
            <div
              className="dataTables_paginate paging_simple_numbers"
              id={id + "_paginate"}
            >
              <ul className="pagination">
                <li
                  className={`paginate_button previous ${
                    page > 1 ? "" : "disabled"
                  }`}
                  aria-controls={id}
                  tabIndex="0"
                  id={id + "_previous"}
                >
                  <a href="#" onClick={e => updatePage(page - 1, e, page > 1)}>
                    Previous
                  </a>
                </li>

                {paginationRange.map(pageNumber => {
                  return (
                    <li
                      key={pageNumber + "_pageNumber"}
                      className={`paginate_button ${
                        pageNumber === page ? "active" : ""
                      }`}
                      aria-controls={id}
                      tabIndex="0"
                    >
                      <a
                        href="#"
                        onClick={e =>
                          updatePage(pageNumber, e, pageNumber !== page)
                        }
                      >
                        {pageNumber}
                      </a>
                    </li>
                  );
                })}

                <li
                  className={`paginate_button next ${
                    page >= higherNumber ? "disabled" : ""
                  }`}
                  aria-controls={id}
                  tabIndex="0"
                  id={id + "_next"}
                >
                  <a
                    href="#"
                    onClick={e => updatePage(page + 1, e, page < higherNumber)}
                  >
                    Next
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
});

Grid.propTypes = {
  showCheckAll: PropTypes.bool,
  config: PropTypes.object.isRequired,
  //data: PropTypes.object.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string
};

export default Grid;
