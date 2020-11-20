//
//  BMSQ_ShopController.h
//  BMSQC
//
//  Created by djx on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_ShopController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate>

@property(nonatomic,assign)int typeInt; //0-所有类型；1-美食；2-咖啡；3-健身；4-娱乐；5-服装；6-其他
@property(nonatomic,assign)BOOL kbShow;

@property (nonatomic,assign)int isSeleType;
@property (nonatomic,strong)NSString *typeStr;  ////////

//广场
@property (nonatomic, strong)NSString *content;
@property (nonatomic, strong)NSString *moduleValue;



@end
