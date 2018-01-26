//package com.ncubo.cas;
//
//import java.util.Optional;
//
//import com.auth0.jwt.interfaces.DecodedJWT;
//
//public class JwtService
//{  
//	public Optional<TempUserProfile> verify(String token){
//		DecodedJWT payload = Jwt.decodificar(token);
//		if(payload != null)
//		{
//			return new TempUserProfile(payload.getPayload());
//		}
//		else 
//		{
//			return null;
//		}
//	}
//}
