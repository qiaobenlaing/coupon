//
//  BMSQ_ShopView.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopView.h"

@interface BMSQ_ShopView ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, assign) int   selectTag;
@end

@implementation BMSQ_ShopView

- (id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.baseView];
        self.m_classifyAry = [@[] mutableCopy];
        self.selectTag = -1;
    }
    return self;
}
#pragma mark --- 解析
- (void)setForDicObject:(NSDictionary *)dicObject{
    NSArray * array = dicObject[@"list"];
    if (array != nil && ![array isKindOfClass:[NSNull class]] && array.count != 0)
    {
        
    for (NSDictionary * newDic in array) {
        BMSQ_circleModel * model = [[BMSQ_circleModel alloc] initWithInforDic:newDic];
        [self.m_classifyAry addObject:model];
    }
    [self.baseView reloadData];
    NSArray * ary = [self.m_classifyAry[0] subList];
    [self.leftDelegate changeValue:ary];
        
    }
}


#pragma mark ----- UITableView
- (UITableView *)baseView{
    if (_baseView == nil) {
        self.baseView = [[UITableView alloc] initWithFrame:self.bounds style:UITableViewStyleGrouped];
        _baseView.rowHeight = 40;
        _baseView.dataSource = self;
        _baseView.delegate = self;
        
    }
    return _baseView;
}
#pragma mark ------ UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (self.m_classifyAry.count == 0) {
        return 0;
    }
    return self.m_classifyAry.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString * cell_id = @"classifyCell";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    BMSQ_circleModel * model = self.m_classifyAry[indexPath.row];
    cell.textLabel.text = model.queryName;
    cell.textLabel.font = [UIFont systemFontOfSize:14];
    
    
    return cell;
}
#pragma mark ------ UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    self.selectTag = (int)indexPath.row;
    if (indexPath.row == 0) {
        NSArray * array = [self.m_classifyAry[0] subList];
        [self.leftDelegate changeValue:array];
    }else if (indexPath.row == 1){
        NSArray * array = [self.m_classifyAry[1] subList];
        [self.leftDelegate changeValue:array];
    }else{
        BMSQ_circleModel * model = self.m_classifyAry[indexPath.row];
        [self.leftDelegate transferValue:0 moduleStr:model.moduleValue queryName:model.queryName];
    }
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 1;
}

@end
