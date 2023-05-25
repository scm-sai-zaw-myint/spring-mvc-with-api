<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>RestFull API | User lists</title>
<script src="https://cdn.tailwindcss.com"></script>
<style type="text/css">
*::-webkit-scrollbar {
  width: 15px;
}
</style>
</head>

<body>
  <section class="bg-gray-50 dark:bg-gray-900 h-full absolute w-full">
    <div class="fixed top-[15px] right-[15px] flex">
      <a
        class="w-10 h-10 rounded-full text-gray-900 font-bold bg-red-600 hover:bg-red-600/30 flex items-center justify-center"
        href="<c:url value="/auth/logout" />"> <img alt="logout"
        class="w-4 h-4"
        src="${pageContext.request.contextPath }/resources/images/logout-icon.svg">
      </a>
    </div>

    <div
      class="relative overflow-y-auto max-h-[60vh]  max-w-[600px] top-[40%] translate-y-[-50%] mx-auto rounded">

      <table
        class="w-full text-sm text-left text-gray-500 dark:text-gray-400 rounded">
        <thead
          class="sticky top-0 text-xs text-gray-700 uppercase bg-gray-200 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" class="px-6 py-3">ID</th>
            <th scope="col" class="px-6 py-3">Name</th>
            <th scope="col" class="px-6 py-3">Role</th>
            <th scope="col" class="px-6 py-3"><a
              class="mx-auto block px-2 py-1 rounded shadow bg-green-600 text-center"
              href="${pageContext.request.contextPath }/user/create">Add</a>
            </th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="user" varStatus="count" items="${usersList }">
            <tr
              class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
              <th scope="row"
                class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                ${user.id }</th>
              <td class="px-6 py-4">${user.name }</td>
              <td class="px-6 py-4"><c:forEach
                  items="${user.roles }" var="role">
                  <span
                    class="m-1 bg-gray-200 p-1 rounded-lg text-gray-900">${role.name}</span>
                </c:forEach></td>
              <td class="px-6 py-4"><a href="update/${user.id }"
                class="text-sky-700 font-bold hover:text-sky-400">Edit</a>
                | <a href="delete/${user.id }"
                class="text-red-700 font-bold hover:text-red-400">Delete</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <c:if test="${sessionMessage != null }">
      <div
        class="fixed bottom-[20px] right-[20px] p-2 rounded-lg shadow text-gray-700 bg-gray-200 dark:bg-gray-700 dark:text-gray-400 flex items-center justify-between">
        <span class="pl-2">${sessionMessage }</span>
        <button
          class="ml-2 close-noti w-8 h-8 flex items-center justify-center bg-gray-400/50 rounded-full">
          <img alt="logout" class="w-4 h-4"
            src="${pageContext.request.contextPath }/resources/images/close-icon.svg">
        </button>
      </div>
      <c:remove var="sessionMessage" />
    </c:if>
  </section>
  <script>
  	var closeBtns = document.getElementsByClassName('close-noti');
  	for(let btn of closeBtns){
  		btn.addEventListener('click', function(){
  			this.parentElement.remove()
  		});
  	}
  </script>
</body>
</html>