<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert</title>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
function kakaopost() {
    new daum.Postcode({
        oncomplete: function(data) {
           document.querySelector("#zipcode").value = data.zonecode;
           document.querySelector("#address").value =  data.address
        }
    }).open();
}
</script>
</head>
<body onresize="resizeTo(800,600)" onload="resizeTo(800,600)">
<form name="zipcode" method="get" action="addAddress.jsp" encType="UTF-8">
    <table border="1">
        <tr>
            <td width="200">우편번호</td>
            <td>
            	<input type="text" name="zipcode" id="zipcode" size="7" readonly> 
                <input type="button" value="우편번호찾기" onclick="kakaopost()">
            </td>
        </tr>
        <tr>
            <td>주소</td>
            <td><input type="text" name="address" id="address" size="70"></td>
        </tr>
    </table>
    <input type="submit" value="제출">
</form>
</body>
</html>