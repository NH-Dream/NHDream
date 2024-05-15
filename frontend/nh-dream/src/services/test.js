import api from "./api";

function test(userId,suc,fail){
  api.post(`sse/notiTest?userId=${userId}`)
  .then(res=>suc(res))
  .catch(err=>fail(err))
}


export { test }