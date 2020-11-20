//
//  OrderDetailsModel.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

/*
 
 clientCode = "91d6c1fc-f5e2-fe5f-13fb-db210c514f9b";
 deliveryAddress = "\U4e2d\U5c71\U8857683\U53f7";
 eatPayType = 1;
 mealNbr = 009;
 orderAmount = "28.00";
 orderCode = "f318e8f4-d5fc-5d03-8730-99efda435f5d";
 orderNbr = 15112700031512046711;
 orderStatus = 24;
 orderTime = "2015-12-04 17:13:35";
 receiver = "\U5b63\U5148\U751f";
 receiverMobileNbr = 15868179748;
 status = 7;
 
 */

@interface OrderDetailsModel : NSObject

@property (nonatomic, strong) NSNumber * count;//数量
@property (nonatomic, strong) NSArray  * orderList;//
@property (nonatomic, strong) NSNumber * orderStatus;//
@property (nonatomic, strong) NSString * title;//标题

- (id)initWithInforDic:(NSDictionary *)inforDic;







@end
