//
//  BMSQ_ShopIndustryView.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopSortingView.h"

@interface BMSQ_ShopSortingView ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;
@property (nonatomic, assign)int seleTag;

@end

@implementation BMSQ_ShopSortingView

- (id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.baseView];
        self.dataSource = [@[] mutableCopy];
        self.seleTag = -1;
    }
    return self;
}

- (void)setForDicSorting:(NSDictionary *)sortingDic{
    NSArray * array = sortingDic[@"list"];
    if (array != nil && ![array isKindOfClass:[NSNull class]] && array.count != 0){
        
    for (NSDictionary * newDic in array) {
        BMSQ_intelligntModel * model = [[BMSQ_intelligntModel alloc] initWithInforDic:newDic];
        [self.dataSource addObject:model];
    }
    [self.baseView reloadData];
        
    }
}

- (UITableView *)baseView{
    if (_baseView == nil) {
        self.baseView = [[UITableView alloc] initWithFrame:self.bounds style:UITableViewStyleGrouped];
        _baseView.rowHeight = 40;
        _baseView.dataSource = self;
        _baseView.delegate = self;
    }
    return _baseView;
}

#pragma mark ----- UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (self.dataSource.count == 0) {
        return 0;
    }
    return self.dataSource.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString * cell_id = @"ShopSortingView";
    SortingViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[SortingViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    BMSQ_intelligntModel * model = self.dataSource[indexPath.row];
    cell.titleLabel.text = model.queryName;
    
    if (self.seleTag == indexPath.row) {
        cell.titleLabel.textColor =UICOLOR(182, 0, 12, 1.0);
        cell.hookImage.frame = CGRectMake(cell.frame.size.width - 20, cell.frame.size.height / 3, 10, cell.frame.size.height / 3);
    }else{
        cell.titleLabel.textColor =UICOLOR(49, 49, 49, 1.0);
        cell.hookImage.frame = CGRectMake(cell.frame.size.width + 20, cell.frame.size.height / 3, 10, cell.frame.size.height / 3);
    }
    
    return cell;
}
#pragma mark -----  UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    self.seleTag = (int)indexPath.row;
    [self.baseView reloadData];
    BMSQ_intelligntModel * model = self.dataSource[indexPath.row];
    [self.delegate sortingViewTheValue:0 valueNumber:model.value];
 
   
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 1;
}


@end
