//
//  BMSQ_Share.h
//  BMSQC
//
//  Created by liuqin on 15/12/30.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <ShareSDK/ShareSDK.h>

@interface BMSQ_Share : NSObject

+(void)shareContent:(NSDictionary *)dicShare;

+(void)shareClick:(NSString *)remark imagePath:(NSString *)imagePath title:(NSString *)title url:(NSString *)url;

@end
