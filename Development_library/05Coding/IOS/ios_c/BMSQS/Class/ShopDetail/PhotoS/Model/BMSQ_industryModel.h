//
//  BMSQ_industryModel.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BMSQ_industryModel : NSObject

@property (nonatomic, strong) NSString * focusedUrl;// 红色图片
@property (nonatomic, strong) NSString * notFocusedUrl;// 灰色图片
@property (nonatomic, strong) NSString * queryName;// 名称
@property (nonatomic, strong) NSNumber *     value;//

- (id)initWithInforDic:(NSDictionary *)inforDic;
@end
