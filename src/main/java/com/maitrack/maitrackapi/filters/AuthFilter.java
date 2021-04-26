package com.maitrack.maitrackapi.filters;

import com.maitrack.maitrackapi.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*FILTERS ARE USED TO INTERCEPT A REQUEST*/
/*WHEN CLIENT TRIES TO ACCESS THE REQUEST, THIS FILTER CODE WILL EXECUTE FIRST*/
/*WE CAN HANDLE THE REQUEST FROM HERE
*   if Validated, then go ahead else not*/
public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization"); //Get the authorization header from client
        if(authHeader != null) {
            String[] authHeaderArr = authHeader.split("Bearer");
            //Ok so authHeader exists, now check if it is in the right format
            if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                try {
                    Claims claims = Jwts.parser().setSigningKey(JWTProperties.getSECRET_KEY())
                            .parseClaimsJws(token).getBody();
                    //this userID field will let us know current userID of the logged in user from anywhere in our code.
                    httpRequest.setAttribute("email", claims.get("email").toString());
                } catch (Exception e) {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid or expired token");
                    return;
                }
            } else { //not the right format
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided in this format-> 'Bearer [token]'");
                return;
            }
        } else { //no header detected
            httpResponse.sendError((HttpStatus.FORBIDDEN.value()), "Authorization token must be provided");
            return;
        }
        //Validation is performed successfully now, so continue processing..
        chain.doFilter(request, response);

    }
}

