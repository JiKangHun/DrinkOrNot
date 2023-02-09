import axios from "axios";

const token = localStorage.getItem("token");

const api = axios.create({
  baseURL: "http://i8a808.p.ssafy.io:8080",
  headers: {
    Authorization:
    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmFiMTIzNCIsImlkIjoxOSwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY3NjA0MjA3NX0.Rk9h_uPYbY1p8KTSICftS6yXNFwfTdb0leMM5I6__vCOWTtE6MxzkPfe9MHAgtCjB1sn0MlUhgQt767TtqO1rQ",
    "Content-Type": "application/json",
  },
});

async function checkCafeList(date, storeName, success, fail) {
  const res = await api
    .get(`/cafe/drinklist`, {
      params: {
        date: date,
        storeName: storeName,
      },
    })
    .then(success)
    .catch(fail);
  return res;
}

export { checkCafeList };
