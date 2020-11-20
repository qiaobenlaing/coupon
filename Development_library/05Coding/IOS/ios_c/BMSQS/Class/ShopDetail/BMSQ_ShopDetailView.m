//
//  BMSQ_ShopDetailView.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopDetailView.h"

@interface BMSQ_ShopDetailView ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;

@end

@implementation BMSQ_ShopDetailView

- (id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
    self.detailAry = [[NSMutableArray alloc]init];
    [self addSubview:self.baseView];
    }
    return self;
}

#pragma mark ---- 解析
- (void)setForArray:(NSArray *)forArray success:(Success)success{
    
    [self.detailAry removeAllObjects];
    [self.detailAry addObjectsFromArray:forArray];
    
    [self.baseView reloadData];
    
    
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
    if (self.detailAry.count == 0) {
        return 0;
    }
    return self.detailAry.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString * cell_id = @"ShopDetailViewCellabcd";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    NSDictionary *dic = [self.detailAry objectAtIndex:indexPath.row];
    NSString * str = [dic objectForKey:@"name"];
    cell.textLabel.text = str;
    cell.textLabel.font = [UIFont systemFontOfSize:14];
    
    return cell;
}
#pragma mark ----- UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSDictionary *dic = [self.detailAry objectAtIndex:indexPath.row];
    NSNumber * moduleValue = [dic objectForKey:@"moduleValue"];
    NSNumber * valueNumber = [dic objectForKey:@"value"];
    [self.delegate detailViewTheValue:0 moduleValue:moduleValue valueNumber:valueNumber];
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 1;
}


@end
