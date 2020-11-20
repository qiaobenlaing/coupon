//
//  BMSQ_filterModel.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BMSQ_filterModel : NSObject

@property (nonatomic, strong) NSString * focusedUrl;//图片
@property (nonatomic, strong) NSString * queryName; //标题
@property (nonatomic, strong) NSNumber *      value;

- (id)initWithInforDic:(NSDictionary *)inforDic;

@end
