//
//  BMSQ_listModel.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BMSQ_circleModel : NSObject

@property (nonatomic, strong) NSString * moduleValue;//
@property (nonatomic, strong) NSString * queryName;//
@property (nonatomic, strong) NSArray  * subList;//

- (id)initWithInforDic:(NSDictionary *)inforDic;

@end
