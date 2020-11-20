//
//  ScreenView.m
//  BMSQS
//
//  Created by  on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ScreenView.h"

@interface ScreenView ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;

@property (nonatomic, strong)NSArray *cellContent;

@end


@implementation ScreenView

- (id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        bgView.backgroundColor = [UIColor grayColor];
        bgView.alpha = 0.4;
        [self addSubview:bgView];
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickBGview)];
        [bgView addGestureRecognizer:tapGesture];
        
        
        self.cellContent = @[@"已下单",@"已接单",@"已派送",@"已送达",@"已撤销",@"待下单",@"全部"];
        [self addSubview:self.baseView];
    }
    return self;
}
-(void)clickBGview{
    if ([self.scDelegate respondsToSelector:@selector(clickBg)]) {
        [self.scDelegate clickBg];
    }

}

- (UITableView *)baseView{
    if (_baseView == nil) {
        self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-120, APP_VIEW_ORIGIN_Y, 120, 40+35*self.cellContent.count) ];
        _baseView.rowHeight = 35;
        _baseView.dataSource = self;
        _baseView.delegate = self;
        _baseView.scrollEnabled = NO;
        
    }
    return _baseView;
}
#pragma mark ---- UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.cellContent.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString * cell_id = @"DataScreenViewCell";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.font = [UIFont systemFontOfSize:11.f];
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        cell.textLabel.textColor = APP_TEXTCOLOR;
    }
    cell.textLabel.text = [self.cellContent objectAtIndex:indexPath.row];
    
    return cell;
    
}
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *Vi = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 120, 40)];
    Vi.backgroundColor = [UIColor whiteColor];
    UIButton *b1 = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 40, 40)];
    [b1 setTitle:@"取消" forState:UIControlStateNormal];
    [b1 setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    
    b1.titleLabel.font = [UIFont systemFontOfSize:12.f];
    [Vi addSubview:b1];
    UIButton *b2 = [[UIButton alloc]initWithFrame:CGRectMake(120-40, 0, 40, 40)];
    [b2 setTitle:@"确定" forState:UIControlStateNormal];
    [b2 setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
     b2.titleLabel.font = [UIFont systemFontOfSize:12.f];
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(40, 0, 40, 40)];
    label.textColor = APP_TEXTCOLOR;
    label.font = [UIFont systemFontOfSize:13.f];
    label.textAlignment = NSTextAlignmentCenter;
    label.text = @"筛选";
    [Vi addSubview:label];
    
    [b1 addTarget:self action:@selector(clickQuit) forControlEvents:UIControlEventTouchUpInside];
//    [b1 addTarget:self action:@selector(clickSumbit) forControlEvents:UIControlEventTouchUpInside];

    [Vi addSubview:b2];
    return Vi;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 40;
}
#pragma mark ---- UITableViewDelegate
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSString *status = @"";
    if (indexPath.row == 0) {  //已下单
        status = @"20";
    }else if (indexPath.row == 1) { //已接单
        status = @"21";
    }else if (indexPath.row == 2) { //已派送
        status = @"22";
    }else if (indexPath.row == 3) { //已送达
        status = @"23";
    }else if (indexPath.row == 4) { //已撤销
        status = @"24";
    }else if(indexPath.row == 5){ //待下单
        status = @"25";
    }else{
        status = @"0";
    }
    
    
    if ([self.scDelegate respondsToSelector:@selector(seleData:)]) {
            [self.scDelegate seleData:status];
    }
}
-(void)clickQuit{
    if ([self.scDelegate respondsToSelector:@selector(clickBg)]) {
        [self.scDelegate clickBg];
    }
}
//-(void)clickSumbit{
//    if ([self.scDelegate respondsToSelector:@selector(seleData:)]) {
////        [self.scDelegate clickBg];
//    }
//}

@end
