<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Restful API | ${user.name }</title>
<script src="https://cdn.tailwindcss.com"></script>

</head>
<body>
  <section class="bg-gray-50 dark:bg-gray-900 h-full absolute w-full">
  <a class="w-10 h-10 fixed top-[15px] right-[15px] rounded-full text-gray-900 font-bold bg-red-600 hover:bg-red-600/30 flex items-center justify-center"
      href="<c:url value="/auth/logout" />"> 
      <img alt="logout" class="w-4 h-4"
      src="${pageContext.request.contextPath }/resources/images/logout-icon.svg">
    </a>
    <div
      class="relative overflow-x-auto w-[500px] my-10 mx-auto rounded">
      <table
        class="w-full text-sm text-left text-gray-500 dark:text-gray-400 rounded">
        <thead
          class="text-xs text-gray-700 uppercase bg-gray-200 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" class="px-6 py-3">ID</th>
            <th scope="col" class="px-6 py-3">Name</th>
            <th scope="col" class="px-6 py-3">Role</th>
            <th scope="col" class="px-6 py-3 text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
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
                class="text-sky-700 font-bold hover:text-sky-400 text-center block mx-auto">Edit</a></td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</body>
</html>