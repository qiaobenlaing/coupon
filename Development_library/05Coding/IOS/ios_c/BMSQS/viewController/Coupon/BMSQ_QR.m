//
//  BMSQ_QR.m
//  BMSQC
//
//  Created by liuqin on 15/9/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_QR.h"
#import "AFJSONRPCClient.h"
#import "AFHTTPRequestOperation.h"
@implementation BMSQ_QR



+(void )getZeroPay:(NSString *)userCouponCode shopCode:(NSString *)shopCode completed:(void(^)(NSDictionary *userInfo,NSString *errMsg))finished{
    
    AFJSONRPCClient *jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_CLIENT_URL]];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:shopCode forKey:@"shopCode"];
    [params setObject:userCouponCode forKey:@"userCouponCode"];
    
    NSString* vcode = [gloabFunction getSign:@"zeroPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [jsonPrcClient invokeMethod:@"zeroPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSDictionary *dic = (NSDictionary *)responseObject;
        
        
        
        if(finished){
            finished(dic,nil);
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        if(finished){
            finished(nil,error.description);
        }
        
    }];
    
}


+(void )getPosPay:(NSString *)userCouponCode shopCode:(NSString *)shopCode platBonus:(NSString *)platBonus shopBonus:(NSString *)shopBonus price:(NSString *)price completed:(void(^)(NSDictionary *userInfo,NSString *errMsg))finished{
    
    AFJSONRPCClient *jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_CLIENT_URL]];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:shopCode forKey:@"shopCode"];
    [params setObject:userCouponCode forKey:@"userCouponCode"];
    
    [params setObject:platBonus forKey:@"platBonus"];
    [params setObject:shopBonus forKey:@"shopBonus"];
    [params setObject:price forKey:@"price"];
    
    
    NSString* vcode = [gloabFunction getSign:@"posPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [jsonPrcClient invokeMethod:@"posPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSDictionary *dic = (NSDictionary *)responseObject;
        
        if(finished){
            finished(dic,nil);
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        if(finished){
            finished(nil,error.description);
        }
        
    }];

    
    
}

@end
