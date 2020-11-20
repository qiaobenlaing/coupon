//
//  BMSQ_ShopIndustryView.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopIndustryView.h"
#import "UIImageView+WebCache.h"
@interface BMSQ_ShopIndustryView ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;
@property (nonatomic, assign) int             selectTag;
@end

@implementation BMSQ_ShopIndustryView

- (id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.baseView];
        self.dataSource = [@[] mutableCopy];
        self.selectTag = -1;
    }
    return self;
}
- (void)setForDicIndustry:(NSDictionary *)IndustryDic{
    NSArray * array = IndustryDic[@"list"];
    if (array != nil && ![array isKindOfClass:[NSNull class]] && array.count != 0){
        for (NSDictionary * newDic in array) {
            BMSQ_industryModel * model = [[BMSQ_industryModel alloc] initWithInforDic:newDic];
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

#pragma mark ----- UITableViewDataSource,
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
    static NSString * cell_id = @"ShopIndustryView";
    IndustryViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[IndustryViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    BMSQ_industryModel * model = self.dataSource[indexPath.row];
    cell.titleLabel.text = model.queryName;
    [cell.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,model.focusedUrl]]placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    
    if (self.selectTag == indexPath.row) {
        [cell.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,model.notFocusedUrl]]placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
        cell.hokImage.frame = CGRectMake(cell.frame.size.width - 20, cell.frame.size.height / 3, 10, cell.frame.size.height / 3);
        cell.titleLabel.textColor = UICOLOR(182, 0, 12, 1.0);
    }else{
        [cell.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,model.focusedUrl]]placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
        cell.hokImage.frame = CGRectMake(cell.frame.size.width + 20, cell.frame.size.height / 3, 10, cell.frame.size.height / 3);
        cell.titleLabel.textColor = UICOLOR(49, 49, 49, 1.0);
    }
    
    return cell;
    
}
#pragma mark ----- UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"111");
    self.selectTag = (int)indexPath.row;
    [self.baseView reloadData];
    BMSQ_industryModel * model = self.dataSource[indexPath.row];
    [self.delegate industryViewTheValue:0 valueNumber:model.value];
    
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 1;
}
@end
